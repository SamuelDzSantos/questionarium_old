import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTurmaComponent } from './add-turma.component';

describe('AddTurmaComponent', () => {
  let component: AddTurmaComponent;
  let fixture: ComponentFixture<AddTurmaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTurmaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddTurmaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
