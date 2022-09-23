import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AddressDetailComponent } from './address-detail.component';

describe('Address Management Detail Component', () => {
  let comp: AddressDetailComponent;
  let fixture: ComponentFixture<AddressDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddressDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ address: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AddressDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AddressDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load address on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.address).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
