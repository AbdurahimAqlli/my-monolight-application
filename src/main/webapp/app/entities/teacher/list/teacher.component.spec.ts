import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TeacherService } from '../service/teacher.service';

import { TeacherComponent } from './teacher.component';

describe('Component Tests', () => {
  describe('Teacher Management Component', () => {
    let comp: TeacherComponent;
    let fixture: ComponentFixture<TeacherComponent>;
    let service: TeacherService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeacherComponent],
      })
        .overrideTemplate(TeacherComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeacherComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TeacherService);

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
      expect(comp.teachers?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
