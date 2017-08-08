/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SmartStatementTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VehiculeDetailComponent } from '../../../../../../main/webapp/app/entities/vehicule/vehicule-detail.component';
import { VehiculeService } from '../../../../../../main/webapp/app/entities/vehicule/vehicule.service';
import { Vehicule } from '../../../../../../main/webapp/app/entities/vehicule/vehicule.model';

describe('Component Tests', () => {

    describe('Vehicule Management Detail Component', () => {
        let comp: VehiculeDetailComponent;
        let fixture: ComponentFixture<VehiculeDetailComponent>;
        let service: VehiculeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SmartStatementTestModule],
                declarations: [VehiculeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VehiculeService,
                    JhiEventManager
                ]
            }).overrideTemplate(VehiculeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VehiculeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VehiculeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Vehicule(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.vehicule).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
