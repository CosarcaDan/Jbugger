import { TestBed } from '@angular/core/testing';

import { BugServiceService } from './bug-service.service';

describe('BugServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BugServiceService = TestBed.get(BugServiceService);
    expect(service).toBeTruthy();
  });
});
