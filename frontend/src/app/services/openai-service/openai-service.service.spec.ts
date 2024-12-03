import { TestBed } from '@angular/core/testing';

import { OpenaiServiceService } from './openai-service.service';

describe('OpenaiServiceService', () => {
  let service: OpenaiServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OpenaiServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
