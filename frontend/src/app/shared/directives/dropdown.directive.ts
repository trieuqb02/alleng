import {Directive, ElementRef, HostListener, Renderer2} from '@angular/core';

@Directive({
  selector: '[appDropdown]',
  standalone: true
})
export class DropdownDirective {

  isOpen:boolean = false;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('document:click', ['$event'])
  toggleOpen(event: Event) {
    const targetElement = event.target as HTMLElement;

    if (this.el.nativeElement.contains(targetElement)) {
      this.isOpen = !this.isOpen;
    } else {
      this.isOpen = false;
    }

    if (this.isOpen) {
      this.renderer.addClass(this.el.nativeElement, 'open');
    } else {
      this.renderer.removeClass(this.el.nativeElement, 'open');
    }
  }

}
