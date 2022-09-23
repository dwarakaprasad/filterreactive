import { ICustomer } from 'app/entities/customer/customer.model';

export interface IAddress {
  id: number;
  street?: string | null;
  city?: string | null;
  state?: string | null;
  zip?: number | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
