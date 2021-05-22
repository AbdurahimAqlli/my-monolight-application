import * as dayjs from 'dayjs';
import { IStudent } from 'app/entities/student/student.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface ICar {
  id?: number;
  model?: string | null;
  carOwnerName?: string | null;
  yearOfCar?: dayjs.Dayjs | null;
  color?: string | null;
  category?: Category | null;
  student?: IStudent | null;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public model?: string | null,
    public carOwnerName?: string | null,
    public yearOfCar?: dayjs.Dayjs | null,
    public color?: string | null,
    public category?: Category | null,
    public student?: IStudent | null
  ) {}
}

export function getCarIdentifier(car: ICar): number | undefined {
  return car.id;
}
