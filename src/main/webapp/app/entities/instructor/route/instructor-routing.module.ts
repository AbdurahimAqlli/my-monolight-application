import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstructorComponent } from '../list/instructor.component';
import { InstructorDetailComponent } from '../detail/instructor-detail.component';
import { InstructorUpdateComponent } from '../update/instructor-update.component';
import { InstructorRoutingResolveService } from './instructor-routing-resolve.service';

const instructorRoute: Routes = [
  {
    path: '',
    component: InstructorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstructorDetailComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstructorUpdateComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstructorUpdateComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(instructorRoute)],
  exports: [RouterModule],
})
export class InstructorRoutingModule {}
