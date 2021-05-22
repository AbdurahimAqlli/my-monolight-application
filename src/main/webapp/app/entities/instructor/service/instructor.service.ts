import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstructor, getInstructorIdentifier } from '../instructor.model';

export type EntityResponseType = HttpResponse<IInstructor>;
export type EntityArrayResponseType = HttpResponse<IInstructor[]>;

@Injectable({ providedIn: 'root' })
export class InstructorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/instructors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(instructor: IInstructor): Observable<EntityResponseType> {
    return this.http.post<IInstructor>(this.resourceUrl, instructor, { observe: 'response' });
  }

  update(instructor: IInstructor): Observable<EntityResponseType> {
    return this.http.put<IInstructor>(`${this.resourceUrl}/${getInstructorIdentifier(instructor) as number}`, instructor, {
      observe: 'response',
    });
  }

  partialUpdate(instructor: IInstructor): Observable<EntityResponseType> {
    return this.http.patch<IInstructor>(`${this.resourceUrl}/${getInstructorIdentifier(instructor) as number}`, instructor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInstructor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstructor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInstructorToCollectionIfMissing(
    instructorCollection: IInstructor[],
    ...instructorsToCheck: (IInstructor | null | undefined)[]
  ): IInstructor[] {
    const instructors: IInstructor[] = instructorsToCheck.filter(isPresent);
    if (instructors.length > 0) {
      const instructorCollectionIdentifiers = instructorCollection.map(instructorItem => getInstructorIdentifier(instructorItem)!);
      const instructorsToAdd = instructors.filter(instructorItem => {
        const instructorIdentifier = getInstructorIdentifier(instructorItem);
        if (instructorIdentifier == null || instructorCollectionIdentifiers.includes(instructorIdentifier)) {
          return false;
        }
        instructorCollectionIdentifiers.push(instructorIdentifier);
        return true;
      });
      return [...instructorsToAdd, ...instructorCollection];
    }
    return instructorCollection;
  }
}
