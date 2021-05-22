import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeacherComponent } from '../list/teacher.component';
import { TeacherDetailComponent } from '../detail/teacher-detail.component';
import { TeacherUpdateComponent } from '../update/teacher-update.component';
import { TeacherRoutingResolveService } from './teacher-routing-resolve.service';

const teacherRoute: Routes = [
  {
    path: '',
    component: TeacherComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeacherDetailComponent,
    resolve: {
      teacher: TeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeacherUpdateComponent,
    resolve: {
      teacher: TeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeacherUpdateComponent,
    resolve: {
      teacher: TeacherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(teacherRoute)],
  exports: [RouterModule],
})
export class TeacherRoutingModule {}
