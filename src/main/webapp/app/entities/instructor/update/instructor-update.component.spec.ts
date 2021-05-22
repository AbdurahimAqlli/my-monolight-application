jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InstructorService } from '../service/instructor.service';
import { IInstructor, Instructor } from '../instructor.model';

import { InstructorUpdateComponent } from './instructor-update.component';

describe('Component Tests', () => {
  describe('Instructor Management Update Component', () => {
    let comp: InstructorUpdateComponent;
    let fixture: ComponentFixture<InstructorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let instructorService: InstructorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InstructorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InstructorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InstructorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      instructorService = TestBed.inject(InstructorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const instructor: IInstructor = { id: 456 };

        activatedRoute.data = of({ instructor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(instructor));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const instructor = { id: 123 };
        spyOn(instructorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ instructor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: instructor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(instructorService.update).toHaveBeenCalledWith(instructor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const instructor = new Instructor();
        spyOn(instructorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ instructor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: instructor }));
        saveSubject.complete();

        // THEN
        expect(instructorService.create).toHaveBeenCalledWith(instructor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const instructor = { id: 123 };
        spyOn(instructorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ instructor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(instructorService.update).toHaveBeenCalledWith(instructor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
