jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITeacher, Teacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';

import { TeacherRoutingResolveService } from './teacher-routing-resolve.service';

describe('Service Tests', () => {
  describe('Teacher routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TeacherRoutingResolveService;
    let service: TeacherService;
    let resultTeacher: ITeacher | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TeacherRoutingResolveService);
      service = TestBed.inject(TeacherService);
      resultTeacher = undefined;
    });

    describe('resolve', () => {
      it('should return ITeacher returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeacher = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeacher).toEqual({ id: 123 });
      });

      it('should return new ITeacher if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeacher = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTeacher).toEqual(new Teacher());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTeacher = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTeacher).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
