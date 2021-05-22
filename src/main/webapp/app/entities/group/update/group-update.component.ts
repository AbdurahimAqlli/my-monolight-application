import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGroup, Group } from '../group.model';
import { GroupService } from '../service/group.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';

@Component({
  selector: 'jhi-group-update',
  templateUrl: './group-update.component.html',
})
export class GroupUpdateComponent implements OnInit {
  isSaving = false;

  teachersSharedCollection: ITeacher[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    category: [],
    beginDrivingDate: [],
    startDate: [],
    endDate: [],
    telegramGroupLink: [],
    status: [],
    teacher: [],
  });

  constructor(
    protected groupService: GroupService,
    protected teacherService: TeacherService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ group }) => {
      if (group.id === undefined) {
        const today = dayjs().startOf('day');
        group.beginDrivingDate = today;
        group.startDate = today;
        group.endDate = today;
      }

      this.updateForm(group);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const group = this.createFromForm();
    if (group.id !== undefined) {
      this.subscribeToSaveResponse(this.groupService.update(group));
    } else {
      this.subscribeToSaveResponse(this.groupService.create(group));
    }
  }

  trackTeacherById(index: number, item: ITeacher): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroup>>): void {
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

  protected updateForm(group: IGroup): void {
    this.editForm.patchValue({
      id: group.id,
      name: group.name,
      category: group.category,
      beginDrivingDate: group.beginDrivingDate ? group.beginDrivingDate.format(DATE_TIME_FORMAT) : null,
      startDate: group.startDate ? group.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: group.endDate ? group.endDate.format(DATE_TIME_FORMAT) : null,
      telegramGroupLink: group.telegramGroupLink,
      status: group.status,
      teacher: group.teacher,
    });

    this.teachersSharedCollection = this.teacherService.addTeacherToCollectionIfMissing(this.teachersSharedCollection, group.teacher);
  }

  protected loadRelationshipsOptions(): void {
    this.teacherService
      .query()
      .pipe(map((res: HttpResponse<ITeacher[]>) => res.body ?? []))
      .pipe(
        map((teachers: ITeacher[]) => this.teacherService.addTeacherToCollectionIfMissing(teachers, this.editForm.get('teacher')!.value))
      )
      .subscribe((teachers: ITeacher[]) => (this.teachersSharedCollection = teachers));
  }

  protected createFromForm(): IGroup {
    return {
      ...new Group(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      category: this.editForm.get(['category'])!.value,
      beginDrivingDate: this.editForm.get(['beginDrivingDate'])!.value
        ? dayjs(this.editForm.get(['beginDrivingDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      telegramGroupLink: this.editForm.get(['telegramGroupLink'])!.value,
      status: this.editForm.get(['status'])!.value,
      teacher: this.editForm.get(['teacher'])!.value,
    };
  }
}
