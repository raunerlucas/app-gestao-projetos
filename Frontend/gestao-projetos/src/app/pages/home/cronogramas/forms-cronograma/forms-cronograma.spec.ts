import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FormsCronograma} from './forms-cronograma';

describe('FormsCronograma', () => {
  let component: FormsCronograma;
  let fixture: ComponentFixture<FormsCronograma>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsCronograma]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormsCronograma);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
