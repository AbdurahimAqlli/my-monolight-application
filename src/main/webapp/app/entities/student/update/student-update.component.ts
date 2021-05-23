import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { IInstructor } from 'app/entities/instructor/instructor.model';
import { InstructorService } from 'app/entities/instructor/service/instructor.service';
import { IGroup } from 'app/entities/group/group.model';
import { GroupService } from 'app/entities/group/service/group.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  instructorsSharedCollection: IInstructor[] = [];
  groupsSharedCollection: IGroup[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    contactNumber: [],
    phoneNumber: [],
    chatId: [],
    studyTime: [],
    category: [],
    instructor: [],
    group: [],
  });

  constructor(
    protected studentService: StudentService,
    protected instructorService: InstructorService,
    protected groupService: GroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackInstructorById(index: number, item: IInstructor): number {
    return item.id!;
  }

  trackGroupById(index: number, item: IGroup): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      name: student.name,
      contactNumber: student.contactNumber,
      phoneNumber: student.phoneNumber,
      chatId: student.chatId,
      studyTime: student.studyTime,
      category: student.category,
      instructor: student.instructor,
      group: student.group,
    });

    this.instructorsSharedCollection = this.instructorService.addInstructorToCollectionIfMissing(
      this.instructorsSharedCollection,
      student.instructor
    );
    this.groupsSharedCollection = this.groupService.addGroupToCollectionIfMissing(this.groupsSharedCollection, student.group);
  }

  protected loadRelationshipsOptions(): void {
    this.instructorService
      .query()
      .pipe(map((res: HttpResponse<IInstructor[]>) => res.body ?? []))
      .pipe(
        map((instructors: IInstructor[]) =>
          this.instructorService.addInstructorToCollectionIfMissing(instructors, this.editForm.get('instructor')!.value)
        )
      )
      .subscribe((instructors: IInstructor[]) => (this.instructorsSharedCollection = instructors));

    this.groupService
      .query()
      .pipe(map((res: HttpResponse<IGroup[]>) => res.body ?? []))
      .pipe(map((groups: IGroup[]) => this.groupService.addGroupToCollectionIfMissing(groups, this.editForm.get('group')!.value)))
      .subscribe((groups: IGroup[]) => (this.groupsSharedCollection = groups));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contactNumber: this.editForm.get(['contactNumber'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      chatId: this.editForm.get(['chatId'])!.value,
      studyTime: this.editForm.get(['studyTime'])!.value,
      category: this.editForm.get(['category'])!.value,
      instructor: this.editForm.get(['instructor'])!.value,
      group: this.editForm.get(['group'])!.value,
    };
  }
}
