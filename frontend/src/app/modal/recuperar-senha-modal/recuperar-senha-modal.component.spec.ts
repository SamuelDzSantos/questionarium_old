import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecuperarSenhaModalComponent } from './recuperar-senha-modal.component';

describe('RecuperarSenhaModalComponent', () => {
  let component: RecuperarSenhaModalComponent;
  let fixture: ComponentFixture<RecuperarSenhaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecuperarSenhaModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecuperarSenhaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
