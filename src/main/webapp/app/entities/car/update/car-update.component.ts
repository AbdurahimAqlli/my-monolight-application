import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICar, Car } from '../car.model';
import { CarService } from '../service/car.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;

  studentsCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    model: [],
    carOwnerName: [],
    yearOfCar: [],
    color: [],
    category: [],
    student: [],
  });

  constructor(
    protected carService: CarService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      if (car.id === undefined) {
        const today = dayjs().startOf('day');
        car.yearOfCar = today;
      }

      this.updateForm(car);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.createFromForm();
    if (car.id !== undefined) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
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

  protected updateForm(car: ICar): void {
    this.editForm.patchValue({
      id: car.id,
      model: car.model,
      carOwnerName: car.carOwnerName,
      yearOfCar: car.yearOfCar ? car.yearOfCar.format(DATE_TIME_FORMAT) : null,
      color: car.color,
      category: car.category,
      student: car.student,
    });

    this.studentsCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsCollection, car.student);
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query({ filter: 'car-is-null' })
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsCollection = students));
  }

  protected createFromForm(): ICar {
    return {
      ...new Car(),
      id: this.editForm.get(['id'])!.value,
      model: this.editForm.get(['model'])!.value,
      carOwnerName: this.editForm.get(['carOwnerName'])!.value,
      yearOfCar: this.editForm.get(['yearOfCar'])!.value ? dayjs(this.editForm.get(['yearOfCar'])!.value, DATE_TIME_FORMAT) : undefined,
      color: this.editForm.get(['color'])!.value,
      category: this.editForm.get(['category'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
