package com.alleng.news.service;

import com.alleng.news.payload.request.NewsVM;
import com.alleng.news.payload.request.PaginationVM;
import com.alleng.news.payload.response.NewsMV;
import com.alleng.news.payload.response.PaginationMV;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface INewsService {
    NewsMV create(MultipartFile image, MultipartFile audio, NewsVM newsVM, String authorizationHeader) throws IOException;

    NewsMV update(UUID newsId, NewsVM newsVM, MultipartFile image, MultipartFile audio, String authorizationHeader) throws IOException;

    List<UUID> delete(List<UUID> newsIdList);

    NewsMV getNews(UUID newsId);

    List<NewsMV> getListRand(int rand);

    PaginationMV<NewsMV> findByNewspaperListByDate(PaginationVM paginationVM);

    PaginationMV<NewsMV> findByNewspaperList(PaginationVM paginationVM);
}
