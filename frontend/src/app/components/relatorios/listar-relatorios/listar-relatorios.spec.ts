import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarRelatoriosComponent } from './listar-relatorios';

describe('ListarRelatorios', () => {
  let component: ListarRelatoriosComponent;
  let fixture: ComponentFixture<ListarRelatoriosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarRelatoriosComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListarRelatoriosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
