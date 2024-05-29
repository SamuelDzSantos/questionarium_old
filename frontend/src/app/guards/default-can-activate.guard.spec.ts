import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { defaultCanActivateGuard } from './default-can-activate.guard';

describe('defaultCanActivateGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => defaultCanActivateGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
