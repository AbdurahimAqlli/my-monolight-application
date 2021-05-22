import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InstructorService } from '../service/instructor.service';

import { InstructorComponent } from './instructor.component';

describe('Component Tests', () => {
  describe('Instructor Management Component', () => {
    let comp: InstructorComponent;
    let fixture: ComponentFixture<InstructorComponent>;
    let service: InstructorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InstructorComponent],
      })
        .overrideTemplate(InstructorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InstructorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InstructorService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.instructors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
