import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';
import { TeacherDeleteDialogComponent } from '../delete/teacher-delete-dialog.component';

@Component({
  selector: 'jhi-teacher',
  templateUrl: './teacher.component.html',
})
export class TeacherComponent implements OnInit {
  teachers?: ITeacher[];
  isLoading = false;

  constructor(protected teacherService: TeacherService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.teacherService.query().subscribe(
      (res: HttpResponse<ITeacher[]>) => {
        this.isLoading = false;
        this.teachers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITeacher): number {
    return item.id!;
  }

  delete(teacher: ITeacher): void {
    const modalRef = this.modalService.open(TeacherDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.teacher = teacher;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
