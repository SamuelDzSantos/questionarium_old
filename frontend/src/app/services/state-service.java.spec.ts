import { TestBed } from '@angular/core/testing';

import { StateServiceJava } from './state-service.java';

describe('StateServiceJava', () => {
  let service: StateServiceJava;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StateServiceJava);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
