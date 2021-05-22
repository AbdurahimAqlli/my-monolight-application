import { IStudent } from 'app/entities/student/student.model';

export interface IInstructor {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  age?: number | null;
  students?: IStudent[] | null;
}

export class Instructor implements IInstructor {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public phoneNumber?: string | null,
    public age?: number | null,
    public students?: IStudent[] | null
  ) {}
}

export function getInstructorIdentifier(instructor: IInstructor): number | undefined {
  return instructor.id;
}
