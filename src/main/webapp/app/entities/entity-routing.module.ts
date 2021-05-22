import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'myMonolightApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'teacher',
        data: { pageTitle: 'myMonolightApp.teacher.home.title' },
        loadChildren: () => import('./teacher/teacher.module').then(m => m.TeacherModule),
      },
      {
        path: 'instructor',
        data: { pageTitle: 'myMonolightApp.instructor.home.title' },
        loadChildren: () => import('./instructor/instructor.module').then(m => m.InstructorModule),
      },
      {
        path: 'group',
        data: { pageTitle: 'myMonolightApp.group.home.title' },
        loadChildren: () => import('./group/group.module').then(m => m.GroupModule),
      },
      {
        path: 'car',
        data: { pageTitle: 'myMonolightApp.car.home.title' },
        loadChildren: () => import('./car/car.module').then(m => m.CarModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
