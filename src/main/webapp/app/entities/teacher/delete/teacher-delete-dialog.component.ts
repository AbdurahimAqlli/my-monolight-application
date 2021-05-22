import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';

@Component({
  templateUrl: './teacher-delete-dialog.component.html',
})
export class TeacherDeleteDialogComponent {
  teacher?: ITeacher;

  constructor(protected teacherService: TeacherService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teacherService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
