import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeacher, getTeacherIdentifier } from '../teacher.model';

export type EntityResponseType = HttpResponse<ITeacher>;
export type EntityArrayResponseType = HttpResponse<ITeacher[]>;

@Injectable({ providedIn: 'root' })
export class TeacherService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/teachers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(teacher: ITeacher): Observable<EntityResponseType> {
    return this.http.post<ITeacher>(this.resourceUrl, teacher, { observe: 'response' });
  }

  update(teacher: ITeacher): Observable<EntityResponseType> {
    return this.http.put<ITeacher>(`${this.resourceUrl}/${getTeacherIdentifier(teacher) as number}`, teacher, { observe: 'response' });
  }

  partialUpdate(teacher: ITeacher): Observable<EntityResponseType> {
    return this.http.patch<ITeacher>(`${this.resourceUrl}/${getTeacherIdentifier(teacher) as number}`, teacher, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeacher>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeacher[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeacherToCollectionIfMissing(teacherCollection: ITeacher[], ...teachersToCheck: (ITeacher | null | undefined)[]): ITeacher[] {
    const teachers: ITeacher[] = teachersToCheck.filter(isPresent);
    if (teachers.length > 0) {
      const teacherCollectionIdentifiers = teacherCollection.map(teacherItem => getTeacherIdentifier(teacherItem)!);
      const teachersToAdd = teachers.filter(teacherItem => {
        const teacherIdentifier = getTeacherIdentifier(teacherItem);
        if (teacherIdentifier == null || teacherCollectionIdentifiers.includes(teacherIdentifier)) {
          return false;
        }
        teacherCollectionIdentifiers.push(teacherIdentifier);
        return true;
      });
      return [...teachersToAdd, ...teacherCollection];
    }
    return teacherCollection;
  }
}
