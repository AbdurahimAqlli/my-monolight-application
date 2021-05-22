jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';
import { IInstructor } from 'app/entities/instructor/instructor.model';
import { InstructorService } from 'app/entities/instructor/service/instructor.service';
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';

import { StudentUpdateComponent } from './student-update.component';

describe('Component Tests', () => {
  describe('Student Management Update Component', () => {
    let comp: StudentUpdateComponent;
    let fixture: ComponentFixture<StudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentService: StudentService;
    let instructorService: InstructorService;
    let groupService: GroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentService = TestBed.inject(StudentService);
      instructorService = TestBed.inject(InstructorService);
      groupService = TestBed.inject(GroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Instructor query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const instructor: IInstructor = { id: 81410 };
        student.instructor = instructor;

        const instructorCollection: IInstructor[] = [{ id: 69756 }];
        spyOn(instructorService, 'query').and.returnValue(of(new HttpResponse({ body: instructorCollection })));
        const additionalInstructors = [instructor];
        const expectedCollection: IInstructor[] = [...additionalInstructors, ...instructorCollection];
        spyOn(instructorService, 'addInstructorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(instructorService.query).toHaveBeenCalled();
        expect(instructorService.addInstructorToCollectionIfMissing).toHaveBeenCalledWith(instructorCollection, ...additionalInstructors);
        expect(comp.instructorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Group query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const group: IGroup = { id: 73316 };
        student.group = group;

        const groupCollection: IGroup[] = [{ id: 54596 }];
        spyOn(groupService, 'query').and.returnValue(of(new HttpResponse({ body: groupCollection })));
        const additionalGroups = [group];
        const expectedCollection: IGroup[] = [...additionalGroups, ...groupCollection];
        spyOn(groupService, 'addGroupToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(groupService.query).toHaveBeenCalled();
        expect(groupService.addGroupToCollectionIfMissing).toHaveBeenCalledWith(groupCollection, ...additionalGroups);
        expect(comp.groupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const student: IStudent = { id: 456 };
        const instructor: IInstructor = { id: 80632 };
        student.instructor = instructor;
        const group: IGroup = { id: 78851 };
        student.group = group;

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(student));
        expect(comp.instructorsSharedCollection).toContain(instructor);
        expect(comp.groupsSharedCollection).toContain(group);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = new Student();
        spyOn(studentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(studentService.create).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInstructorById', () => {
        it('Should return tracked Instructor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstructorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackGroupById', () => {
        it('Should return tracked Group primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
