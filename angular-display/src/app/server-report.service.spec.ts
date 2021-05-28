import { TestBed } from '@angular/core/testing';

import { ServerReportService } from './server-report.service';

describe('ServerReportService', () => {
  let service: ServerReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServerReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
