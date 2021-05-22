import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstructor } from '../instructor.model';

@Component({
  selector: 'jhi-instructor-detail',
  templateUrl: './instructor-detail.component.html',
})
export class InstructorDetailComponent implements OnInit {
  instructor: IInstructor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instructor }) => {
      this.instructor = instructor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
