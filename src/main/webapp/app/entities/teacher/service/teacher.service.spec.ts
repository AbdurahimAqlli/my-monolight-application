import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITeacher, Teacher } from '../teacher.model';

import { TeacherService } from './teacher.service';

describe('Service Tests', () => {
  describe('Teacher Service', () => {
    let service: TeacherService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeacher;
    let expectedResult: ITeacher | ITeacher[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TeacherService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        firstName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        chatId: 'AAAAAAA',
        age: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Teacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Teacher()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Teacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            chatId: 'BBBBBB',
            age: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Teacher', () => {
        const patchObject = Object.assign(
          {
            firstName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            chatId: 'BBBBBB',
            age: 1,
          },
          new Teacher()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Teacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            chatId: 'BBBBBB',
            age: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Teacher', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTeacherToCollectionIfMissing', () => {
        it('should add a Teacher to an empty array', () => {
          const teacher: ITeacher = { id: 123 };
          expectedResult = service.addTeacherToCollectionIfMissing([], teacher);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teacher);
        });

        it('should not add a Teacher to an array that contains it', () => {
          const teacher: ITeacher = { id: 123 };
          const teacherCollection: ITeacher[] = [
            {
              ...teacher,
            },
            { id: 456 },
          ];
          expectedResult = service.addTeacherToCollectionIfMissing(teacherCollection, teacher);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Teacher to an array that doesn't contain it", () => {
          const teacher: ITeacher = { id: 123 };
          const teacherCollection: ITeacher[] = [{ id: 456 }];
          expectedResult = service.addTeacherToCollectionIfMissing(teacherCollection, teacher);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teacher);
        });

        it('should add only unique Teacher to an array', () => {
          const teacherArray: ITeacher[] = [{ id: 123 }, { id: 456 }, { id: 64787 }];
          const teacherCollection: ITeacher[] = [{ id: 123 }];
          expectedResult = service.addTeacherToCollectionIfMissing(teacherCollection, ...teacherArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const teacher: ITeacher = { id: 123 };
          const teacher2: ITeacher = { id: 456 };
          expectedResult = service.addTeacherToCollectionIfMissing([], teacher, teacher2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(teacher);
          expect(expectedResult).toContain(teacher2);
        });

        it('should accept null and undefined values', () => {
          const teacher: ITeacher = { id: 123 };
          expectedResult = service.addTeacherToCollectionIfMissing([], null, teacher, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(teacher);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
