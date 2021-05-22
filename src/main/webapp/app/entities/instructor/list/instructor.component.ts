import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';
import { InstructorDeleteDialogComponent } from '../delete/instructor-delete-dialog.component';

@Component({
  selector: 'jhi-instructor',
  templateUrl: './instructor.component.html',
})
export class InstructorComponent implements OnInit {
  instructors?: IInstructor[];
  isLoading = false;

  constructor(protected instructorService: InstructorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.instructorService.query().subscribe(
      (res: HttpResponse<IInstructor[]>) => {
        this.isLoading = false;
        this.instructors = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInstructor): number {
    return item.id!;
  }

  delete(instructor: IInstructor): void {
    const modalRef = this.modalService.open(InstructorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.instructor = instructor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
