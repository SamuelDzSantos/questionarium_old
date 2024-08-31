import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecuperarSenhaComponent } from './recuperar-senha.component';
import { beforeEach, describe, it } from 'node:test';

describe('RecuperarSenhaComponent', () => {
  let component: RecuperarSenhaComponent;
  let fixture: ComponentFixture<RecuperarSenhaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecuperarSenhaComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RecuperarSenhaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
