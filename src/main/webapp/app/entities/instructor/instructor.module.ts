import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InstructorComponent } from './list/instructor.component';
import { InstructorDetailComponent } from './detail/instructor-detail.component';
import { InstructorUpdateComponent } from './update/instructor-update.component';
import { InstructorDeleteDialogComponent } from './delete/instructor-delete-dialog.component';
import { InstructorRoutingModule } from './route/instructor-routing.module';

@NgModule({
  imports: [SharedModule, InstructorRoutingModule],
  declarations: [InstructorComponent, InstructorDetailComponent, InstructorUpdateComponent, InstructorDeleteDialogComponent],
  entryComponents: [InstructorDeleteDialogComponent],
})
export class InstructorModule {}
