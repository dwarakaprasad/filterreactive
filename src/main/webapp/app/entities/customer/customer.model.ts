import dayjs from 'dayjs/esm';
import { CustomerType } from 'app/entities/enumerations/customer-type.model';

export interface ICustomer {
  id: number;
  name?: string | null;
  age?: number | null;
  distanceTravelled?: number | null;
  amountSpent?: number | null;
  amountSaved?: number | null;
  amountEarned?: number | null;
  happyPerson?: boolean | null;
  dob?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
  travelDate?: dayjs.Dayjs | null;
  travelTime?: string | null;
  customerType?: CustomerType | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
