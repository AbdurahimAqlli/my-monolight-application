import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InstructorDetailComponent } from './instructor-detail.component';

describe('Component Tests', () => {
  describe('Instructor Management Detail Component', () => {
    let comp: InstructorDetailComponent;
    let fixture: ComponentFixture<InstructorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InstructorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ instructor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InstructorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InstructorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load instructor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.instructor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
