jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeacherService } from '../service/teacher.service';
import { ITeacher, Teacher } from '../teacher.model';

import { TeacherUpdateComponent } from './teacher-update.component';

describe('Component Tests', () => {
  describe('Teacher Management Update Component', () => {
    let comp: TeacherUpdateComponent;
    let fixture: ComponentFixture<TeacherUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teacherService: TeacherService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeacherUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeacherUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeacherUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teacherService = TestBed.inject(TeacherService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const teacher: ITeacher = { id: 456 };

        activatedRoute.data = of({ teacher });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(teacher));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teacher = { id: 123 };
        spyOn(teacherService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teacher }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teacherService.update).toHaveBeenCalledWith(teacher);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teacher = new Teacher();
        spyOn(teacherService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teacher }));
        saveSubject.complete();

        // THEN
        expect(teacherService.create).toHaveBeenCalledWith(teacher);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teacher = { id: 123 };
        spyOn(teacherService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teacher });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teacherService.update).toHaveBeenCalledWith(teacher);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
