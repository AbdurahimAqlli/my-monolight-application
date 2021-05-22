import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICar, getCarIdentifier } from '../car.model';

export type EntityResponseType = HttpResponse<ICar>;
export type EntityArrayResponseType = HttpResponse<ICar[]>;

@Injectable({ providedIn: 'root' })
export class CarService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cars');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(car: ICar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http
      .post<ICar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(car: ICar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http
      .put<ICar>(`${this.resourceUrl}/${getCarIdentifier(car) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(car: ICar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(car);
    return this.http
      .patch<ICar>(`${this.resourceUrl}/${getCarIdentifier(car) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarToCollectionIfMissing(carCollection: ICar[], ...carsToCheck: (ICar | null | undefined)[]): ICar[] {
    const cars: ICar[] = carsToCheck.filter(isPresent);
    if (cars.length > 0) {
      const carCollectionIdentifiers = carCollection.map(carItem => getCarIdentifier(carItem)!);
      const carsToAdd = cars.filter(carItem => {
        const carIdentifier = getCarIdentifier(carItem);
        if (carIdentifier == null || carCollectionIdentifiers.includes(carIdentifier)) {
          return false;
        }
        carCollectionIdentifiers.push(carIdentifier);
        return true;
      });
      return [...carsToAdd, ...carCollection];
    }
    return carCollection;
  }

  protected convertDateFromClient(car: ICar): ICar {
    return Object.assign({}, car, {
      yearOfCar: car.yearOfCar?.isValid() ? car.yearOfCar.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.yearOfCar = res.body.yearOfCar ? dayjs(res.body.yearOfCar) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((car: ICar) => {
        car.yearOfCar = car.yearOfCar ? dayjs(car.yearOfCar) : undefined;
      });
    }
    return res;
  }
}
