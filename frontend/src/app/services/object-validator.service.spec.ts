import { TestBed } from '@angular/core/testing';

import { ObjectValidatorService } from './object-validator.service';

describe('ObjectValidatorService', () => {
  let service: ObjectValidatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObjectValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
