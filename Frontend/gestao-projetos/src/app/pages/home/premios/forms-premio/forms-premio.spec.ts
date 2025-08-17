import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsPremio} from './forms-premio';

describe('FormsPremio', () => {
  let component: FormsPremio;
  let fixture: ComponentFixture<FormsPremio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsPremio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormsPremio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
