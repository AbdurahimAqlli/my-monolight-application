import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';

@Component({
  templateUrl: './instructor-delete-dialog.component.html',
})
export class InstructorDeleteDialogComponent {
  instructor?: IInstructor;

  constructor(protected instructorService: InstructorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instructorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
