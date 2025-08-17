import {TestBed} from '@angular/core/testing';

import {Projetos} from './projetosService';

describe('Projetos', () => {
  let service: Projetos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Projetos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
