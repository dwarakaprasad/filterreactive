import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 88754,
};

export const sampleWithPartialData: IAddress = {
  id: 72042,
  street: 'Joanie Square',
  city: 'Port Sophieview',
};

export const sampleWithFullData: IAddress = {
  id: 11017,
  street: 'Tyrese Walks',
  city: 'Garden Grove',
  state: 'efficient Montana',
  zip: 83280,
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
