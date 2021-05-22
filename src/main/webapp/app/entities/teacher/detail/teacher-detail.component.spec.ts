import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeacherDetailComponent } from './teacher-detail.component';

describe('Component Tests', () => {
  describe('Teacher Management Detail Component', () => {
    let comp: TeacherDetailComponent;
    let fixture: ComponentFixture<TeacherDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TeacherDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ teacher: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TeacherDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeacherDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load teacher on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teacher).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
