package com.alleng.news.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.news.constant.EventType;
import com.alleng.news.constant.PaginationConstant;
import com.alleng.news.constant.StatusType;
import com.alleng.news.entity.*;
import com.alleng.news.payload.request.NewsVM;
import com.alleng.news.payload.request.PaginationVM;
import com.alleng.news.payload.request.ParagraphVM;
import com.alleng.news.payload.response.NewsMV;
import com.alleng.news.payload.response.PaginationMV;
import com.alleng.news.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NewsService implements INewsService {

    NewsRepository newsRepository;

    TopicRepository topicRepository;

    SourceRepository sourceRepository;

    OutboxRepository outboxRepository;

    PasserUtil passerUtil;

    ParagraphRepository paragraphRepository;

    @Transactional
    @Override
    public NewsMV create(MultipartFile image, MultipartFile audio, NewsVM newsVM, String authorizationHeader) throws IOException {
        Topic topic = topicRepository.findById(newsVM.topicId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOPIC_NOT_FOUND, newsVM.topicId()));

        Source source = sourceRepository.findById(newsVM.sourceId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SOURCE_NOT_FOUND, newsVM.sourceId()));

        News news = newsRepository.save(
                News.builder()
                        .title(newsVM.title())
                        .status(newsVM.status())
                        .publicationDate(newsVM.publicationDate())
                        .enable(newsVM.enable())
                        .status(StatusType.PROCESSING)
                        .source(source)
                        .topic(topic)
                        .build()
        );

        News finalNews = news;
        List<Paragraph> paragraphs = newsVM.paragraphs()
                .stream()
                .map(s -> new Paragraph(s.content(), finalNews))
                .collect(Collectors.toList());

        news.setParagraphs(paragraphs);

        news = newsRepository.save(news);

        String imageBase64 = passerUtil.convertByteToBase64(image.getBytes());
        String audioBase64 = passerUtil.convertByteToBase64(audio.getBytes());
        String payload = new ObjectMapper().writeValueAsString(Map.of(
                "token", authorizationHeader,
                "newsId", news.getId().toString(),
                "image", imageBase64,
                "audio", audioBase64,
                "imageName", "",
                "audioName", ""
        ));

        Outbox outbox = Outbox.builder()
                .processedAt(Instant.now())
                .aggregateType("entity")
                .aggregateId(news.getId())
                .eventType(EventType.SAVE_FILE)
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .build();

        outboxRepository.save(outbox);

        return NewsMV.convertNewsMV(news);
    }

    @Override
    public NewsMV update(UUID newsId, NewsVM newsVM, MultipartFile image, MultipartFile audio, String authorizationHeader) throws IOException {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NEWS_NOT_FOUND, newsId));

        if (!newsVM.topicId().equals(news.getTopic().getId())) {
            Topic topic = topicRepository.findById(newsVM.topicId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.TOPIC_NOT_FOUND, newsVM.topicId()));
            news.setTopic(topic);
        }

        if (!newsVM.sourceId().equals(news.getSource().getId())) {
            Source source = sourceRepository.findById(newsVM.sourceId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.SOURCE_NOT_FOUND, newsVM.sourceId()));
            news.setSource(source);
        }

        news.setEnable(newsVM.enable());
        news.setTitle(newsVM.title());
        news.setPublicationDate(newsVM.publicationDate());

        List<Paragraph> updatedParagraphs = new ArrayList<>();

        for (ParagraphVM paragraphVM : newsVM.paragraphs()) {
            Paragraph paragraph;
            if (paragraphVM.id() == null) {
                paragraph = new Paragraph(paragraphVM.content(), news);
            } else {
                paragraph = paragraphRepository.findById(paragraphVM.id())
                        .orElse(new Paragraph(paragraphVM.content(), news));
                paragraph.setContent(paragraphVM.content());
            }
            updatedParagraphs.add(paragraph);
        }

        news.setParagraphs(updatedParagraphs);

        String imageBase64 = (image != null) ? passerUtil.convertByteToBase64(image.getBytes()) : "";
        String audioBase64 = (audio != null) ? passerUtil.convertByteToBase64(audio.getBytes()) : "";
        boolean checkUploadFile = !imageBase64.isEmpty() || !audioBase64.isEmpty();

        if (checkUploadFile) {
            String imageName = news.getThumbnail().substring(news.getThumbnail().lastIndexOf("/") + 1);
            String audioName = news.getAudio().substring(news.getAudio().lastIndexOf("/") + 1);
            String payload = new ObjectMapper().writeValueAsString(Map.of(
                    "token", authorizationHeader,
                    "newsId", news.getId().toString(),
                    "image", imageBase64,
                    "audio", audioBase64,
                    "imageName", !imageBase64.isEmpty() ? imageName : "",
                    "audioName", !audioBase64.isEmpty() ? audioName : ""
            ));

            Outbox outbox = Outbox.builder()
                    .processedAt(Instant.now())
                    .aggregateType("entity")
                    .aggregateId(news.getId())
                    .eventType(EventType.UPDATE_FILE)
                    .payload(payload)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxRepository.save(outbox);
        }

        return NewsMV.convertNewsMV(newsRepository.save(news));
    }

    @Override
    @Transactional
    public List<UUID> delete(List<UUID> newsIdList) {
        List<News> newsList = newsRepository.findAllById(newsIdList);

        List<UUID> deleteNewsId = new LinkedList<>();
        List<Outbox> outboxList = new LinkedList<>();
        newsList.forEach(news -> {

            deleteNewsId.add(news.getId());
            String image = news.getThumbnail().substring(news.getThumbnail().lastIndexOf("/") + 1);
            String audio = news.getAudio().substring(news.getAudio().lastIndexOf("/") + 1);
            String payload;
            try {
                payload = new ObjectMapper().writeValueAsString(Map.of(
                        "token", "",
                        "newsId", news.getId().toString(),
                        "image", "",
                        "audio", "",
                        "imageName", image,
                        "audioName", audio
                ));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            Outbox outbox = Outbox.builder()
                    .processedAt(Instant.now())
                    .aggregateType("entity")
                    .aggregateId(news.getId())
                    .eventType(EventType.DELETE_FILE)
                    .payload(payload)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxList.add(outbox);
        });

        outboxRepository.saveAll(outboxList);
        newsRepository.deleteAll(newsList);
        return deleteNewsId;
    }

    @Override
    public NewsMV getNews(UUID newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NEWS_NOT_FOUND, newsId));
        return NewsMV.convertNewsMV(news);
    }

    @Override
    public List<NewsMV> getListRand(int rand) {
        PageRequest pageRequest = PageRequest.of(0, rand);
        List<News> list = newsRepository.findRandomNews(pageRequest);
        return list.stream().map(NewsMV::convertNewsMV).collect(Collectors.toList());
    }

    @Override
    public PaginationMV<NewsMV> findByNewspaperListByDate(PaginationVM paginationVM) {
        Sort sort = paginationVM.sortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(paginationVM.sortBy()).ascending() : Sort.by(paginationVM.sortBy()).descending();

        Pageable pageable = PageRequest.of(paginationVM.page() - 1, paginationVM.limit(), sort);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(paginationVM.date(), formatter);

        LocalDate nextLocalDate = startLocalDate.plusDays(1);

        Date startDay = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date nextDay = Date.from(nextLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<News> resultPage = newsRepository.findByPublicationDateBetween(startDay, nextDay, pageable);

        List<NewsMV> collect = resultPage.getContent().stream().map(NewsMV::convertNewsMV).collect(Collectors.toList());

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }

    @Override
    public PaginationMV<NewsMV> findByNewspaperList(PaginationVM paginationVM) {
        Sort sort = paginationVM.sortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(paginationVM.sortBy()).ascending() : Sort.by(paginationVM.sortBy()).descending();

        Pageable pageable = PageRequest.of(paginationVM.page() - 1, paginationVM.limit(), sort);

        Example<News> newsExample;
        Page<News> resultPage;
        News news = new News();
        if (!paginationVM.topic().equals(PaginationConstant.DEFAULT_ALL)) {
            Topic topic = topicRepository.findByName(paginationVM.topic())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.TOPIC_NOT_FOUND));
            news.setTopic(topic);
        }
        if (!paginationVM.source().equals(PaginationConstant.DEFAULT_ALL)) {
            Source source = sourceRepository.findByName(paginationVM.source())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.TOPIC_NOT_FOUND));
            news.setSource(source);
        }
        if (paginationVM.search() != null && !paginationVM.search().isEmpty()) {
            news.setTitle(paginationVM.search());

            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase();

            newsExample = Example.of(news, matcher);
            resultPage = newsRepository.findAll(newsExample, pageable);
        } else {
            resultPage = newsRepository.findAll(pageable);
        }

        List<NewsMV> collect = resultPage.getContent().stream().map(NewsMV::convertNewsMV).collect(Collectors.toList());

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }

}
