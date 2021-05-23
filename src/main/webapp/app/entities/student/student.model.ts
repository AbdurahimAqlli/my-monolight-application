import { ICar } from 'app/entities/car/car.model';
import { IInstructor } from 'app/entities/instructor/instructor.model';
import { IGroup } from 'app/entities/group/group.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface IStudent {
  id?: number;
  name?: string | null;
  contactNumber?: string | null;
  phoneNumber?: string | null;
  chatId?: string | null;
  studyTime?: string | null;
  category?: Category | null;
  car?: ICar | null;
  instructor?: IInstructor | null;
  group?: IGroup | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public name?: string | null,
    public contactNumber?: string | null,
    public phoneNumber?: string | null,
    public chatId?: string | null,
    public studyTime?: string | null,
    public category?: Category | null,
    public car?: ICar | null,
    public instructor?: IInstructor | null,
    public group?: IGroup | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
