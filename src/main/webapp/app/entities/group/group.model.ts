import * as dayjs from 'dayjs';
import { IStudent } from 'app/entities/student/student.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { Category } from 'app/entities/enumerations/category.model';
import { GroupStatus } from 'app/entities/enumerations/group-status.model';

export interface IGroup {
  id?: number;
  name?: string | null;
  category?: Category | null;
  beginDrivingDate?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  telegramGroupLink?: string | null;
  status?: GroupStatus | null;
  students?: IStudent[] | null;
  teacher?: ITeacher | null;
}

export class Group implements IGroup {
  constructor(
    public id?: number,
    public name?: string | null,
    public category?: Category | null,
    public beginDrivingDate?: dayjs.Dayjs | null,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public telegramGroupLink?: string | null,
    public status?: GroupStatus | null,
    public students?: IStudent[] | null,
    public teacher?: ITeacher | null
  ) {}
}

export function getGroupIdentifier(group: IGroup): number | undefined {
  return group.id;
}
