import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInstructor, Instructor } from '../instructor.model';

import { InstructorService } from './instructor.service';

describe('Service Tests', () => {
  describe('Instructor Service', () => {
    let service: InstructorService;
    let httpMock: HttpTestingController;
    let elemDefault: IInstructor;
    let expectedResult: IInstructor | IInstructor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InstructorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        firstName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
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

      it('should create a Instructor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Instructor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Instructor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
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

      it('should partial update a Instructor', () => {
        const patchObject = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            age: 1,
          },
          new Instructor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Instructor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
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

      it('should delete a Instructor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInstructorToCollectionIfMissing', () => {
        it('should add a Instructor to an empty array', () => {
          const instructor: IInstructor = { id: 123 };
          expectedResult = service.addInstructorToCollectionIfMissing([], instructor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instructor);
        });

        it('should not add a Instructor to an array that contains it', () => {
          const instructor: IInstructor = { id: 123 };
          const instructorCollection: IInstructor[] = [
            {
              ...instructor,
            },
            { id: 456 },
          ];
          expectedResult = service.addInstructorToCollectionIfMissing(instructorCollection, instructor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Instructor to an array that doesn't contain it", () => {
          const instructor: IInstructor = { id: 123 };
          const instructorCollection: IInstructor[] = [{ id: 456 }];
          expectedResult = service.addInstructorToCollectionIfMissing(instructorCollection, instructor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instructor);
        });

        it('should add only unique Instructor to an array', () => {
          const instructorArray: IInstructor[] = [{ id: 123 }, { id: 456 }, { id: 48160 }];
          const instructorCollection: IInstructor[] = [{ id: 123 }];
          expectedResult = service.addInstructorToCollectionIfMissing(instructorCollection, ...instructorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const instructor: IInstructor = { id: 123 };
          const instructor2: IInstructor = { id: 456 };
          expectedResult = service.addInstructorToCollectionIfMissing([], instructor, instructor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instructor);
          expect(expectedResult).toContain(instructor2);
        });

        it('should accept null and undefined values', () => {
          const instructor: IInstructor = { id: 123 };
          expectedResult = service.addInstructorToCollectionIfMissing([], null, instructor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instructor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
