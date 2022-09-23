import dayjs from 'dayjs/esm';

import { CustomerType } from 'app/entities/enumerations/customer-type.model';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
};

export const sampleWithPartialData: ICustomer = {
  id: 75018,
  age: 54302,
  distanceTravelled: 52057,
  amountEarned: 61357,
  happyPerson: true,
  dob: dayjs('2022-09-23T05:55'),
  customerType: CustomerType['VP'],
};

export const sampleWithFullData: ICustomer = {
  id: 58190,
  name: 'Chips',
  age: 36162,
  distanceTravelled: 12349,
  amountSpent: 43545,
  amountSaved: 87783,
  amountEarned: 63345,
  happyPerson: true,
  dob: dayjs('2022-09-23T04:45'),
  createdDate: dayjs('2022-09-23'),
  travelDate: dayjs('2022-09-23T10:37'),
  travelTime: '76936',
  customerType: CustomerType['VP'],
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
