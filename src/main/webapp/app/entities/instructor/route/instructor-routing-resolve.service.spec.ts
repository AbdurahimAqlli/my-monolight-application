jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstructor, Instructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';

import { InstructorRoutingResolveService } from './instructor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Instructor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InstructorRoutingResolveService;
    let service: InstructorService;
    let resultInstructor: IInstructor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InstructorRoutingResolveService);
      service = TestBed.inject(InstructorService);
      resultInstructor = undefined;
    });

    describe('resolve', () => {
      it('should return IInstructor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstructor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInstructor).toEqual({ id: 123 });
      });

      it('should return new IInstructor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstructor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInstructor).toEqual(new Instructor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstructor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInstructor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
