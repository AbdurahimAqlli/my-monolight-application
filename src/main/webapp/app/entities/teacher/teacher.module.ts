import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TeacherComponent } from './list/teacher.component';
import { TeacherDetailComponent } from './detail/teacher-detail.component';
import { TeacherUpdateComponent } from './update/teacher-update.component';
import { TeacherDeleteDialogComponent } from './delete/teacher-delete-dialog.component';
import { TeacherRoutingModule } from './route/teacher-routing.module';

@NgModule({
  imports: [SharedModule, TeacherRoutingModule],
  declarations: [TeacherComponent, TeacherDetailComponent, TeacherUpdateComponent, TeacherDeleteDialogComponent],
  entryComponents: [TeacherDeleteDialogComponent],
})
export class TeacherModule {}
