import {TestBed} from '@angular/core/testing';

import {CronogramaSelectService} from './cronograma-select-service';

describe('CronogramaSelectService', () => {
  let service: CronogramaSelectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CronogramaSelectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
