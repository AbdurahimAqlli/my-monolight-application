import { IGroup } from 'app/entities/group/group.model';

export interface ITeacher {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  age?: number | null;
  groups?: IGroup[] | null;
}

export class Teacher implements ITeacher {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public phoneNumber?: string | null,
    public age?: number | null,
    public groups?: IGroup[] | null
  ) {}
}

export function getTeacherIdentifier(teacher: ITeacher): number | undefined {
  return teacher.id;
}
