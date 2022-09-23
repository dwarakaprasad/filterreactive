import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomer | NewCustomer> = Omit<T, 'dob' | 'travelDate'> & {
  dob?: string | null;
  travelDate?: string | null;
};

type CustomerFormRawValue = FormValueOf<ICustomer>;

type NewCustomerFormRawValue = FormValueOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'happyPerson' | 'dob' | 'travelDate'>;

type CustomerFormGroupContent = {
  id: FormControl<CustomerFormRawValue['id'] | NewCustomer['id']>;
  name: FormControl<CustomerFormRawValue['name']>;
  age: FormControl<CustomerFormRawValue['age']>;
  distanceTravelled: FormControl<CustomerFormRawValue['distanceTravelled']>;
  amountSpent: FormControl<CustomerFormRawValue['amountSpent']>;
  amountSaved: FormControl<CustomerFormRawValue['amountSaved']>;
  amountEarned: FormControl<CustomerFormRawValue['amountEarned']>;
  happyPerson: FormControl<CustomerFormRawValue['happyPerson']>;
  dob: FormControl<CustomerFormRawValue['dob']>;
  createdDate: FormControl<CustomerFormRawValue['createdDate']>;
  travelDate: FormControl<CustomerFormRawValue['travelDate']>;
  travelTime: FormControl<CustomerFormRawValue['travelTime']>;
  customerType: FormControl<CustomerFormRawValue['customerType']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = this.convertCustomerToCustomerRawValue({
      ...this.getFormDefaults(),
      ...customer,
    });
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(customerRawValue.name),
      age: new FormControl(customerRawValue.age),
      distanceTravelled: new FormControl(customerRawValue.distanceTravelled),
      amountSpent: new FormControl(customerRawValue.amountSpent),
      amountSaved: new FormControl(customerRawValue.amountSaved),
      amountEarned: new FormControl(customerRawValue.amountEarned),
      happyPerson: new FormControl(customerRawValue.happyPerson),
      dob: new FormControl(customerRawValue.dob),
      createdDate: new FormControl(customerRawValue.createdDate),
      travelDate: new FormControl(customerRawValue.travelDate),
      travelTime: new FormControl(customerRawValue.travelTime),
      customerType: new FormControl(customerRawValue.customerType),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return this.convertCustomerRawValueToCustomer(form.getRawValue() as CustomerFormRawValue | NewCustomerFormRawValue);
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = this.convertCustomerToCustomerRawValue({ ...this.getFormDefaults(), ...customer });
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      happyPerson: false,
      dob: currentTime,
      travelDate: currentTime,
    };
  }

  private convertCustomerRawValueToCustomer(rawCustomer: CustomerFormRawValue | NewCustomerFormRawValue): ICustomer | NewCustomer {
    return {
      ...rawCustomer,
      dob: dayjs(rawCustomer.dob, DATE_TIME_FORMAT),
      travelDate: dayjs(rawCustomer.travelDate, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerToCustomerRawValue(
    customer: ICustomer | (Partial<NewCustomer> & CustomerFormDefaults)
  ): CustomerFormRawValue | PartialWithRequiredKeyOf<NewCustomerFormRawValue> {
    return {
      ...customer,
      dob: customer.dob ? customer.dob.format(DATE_TIME_FORMAT) : undefined,
      travelDate: customer.travelDate ? customer.travelDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
