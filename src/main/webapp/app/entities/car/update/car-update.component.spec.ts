jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CarService } from '../service/car.service';
import { ICar, Car } from '../car.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { CarUpdateComponent } from './car-update.component';

describe('Component Tests', () => {
  describe('Car Management Update Component', () => {
    let comp: CarUpdateComponent;
    let fixture: ComponentFixture<CarUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let carService: CarService;
    let studentService: StudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CarUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      carService = TestBed.inject(CarService);
      studentService = TestBed.inject(StudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call student query and add missing value', () => {
        const car: ICar = { id: 456 };
        const student: IStudent = { id: 86635 };
        car.student = student;

        const studentCollection: IStudent[] = [{ id: 13984 }];
        spyOn(studentService, 'query').and.returnValue(of(new HttpResponse({ body: studentCollection })));
        const expectedCollection: IStudent[] = [student, ...studentCollection];
        spyOn(studentService, 'addStudentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ car });
        comp.ngOnInit();

        expect(studentService.query).toHaveBeenCalled();
        expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, student);
        expect(comp.studentsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const car: ICar = { id: 456 };
        const student: IStudent = { id: 37589 };
        car.student = student;

        activatedRoute.data = of({ car });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(car));
        expect(comp.studentsCollection).toContain(student);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const car = { id: 123 };
        spyOn(carService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ car });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: car }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(carService.update).toHaveBeenCalledWith(car);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const car = new Car();
        spyOn(carService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ car });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: car }));
        saveSubject.complete();

        // THEN
        expect(carService.create).toHaveBeenCalledWith(car);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const car = { id: 123 };
        spyOn(carService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ car });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(carService.update).toHaveBeenCalledWith(car);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackStudentById', () => {
        it('Should return tracked Student primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
