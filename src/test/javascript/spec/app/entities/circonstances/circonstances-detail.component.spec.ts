/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SmartStatementTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CirconstancesDetailComponent } from '../../../../../../main/webapp/app/entities/circonstances/circonstances-detail.component';
import { CirconstancesService } from '../../../../../../main/webapp/app/entities/circonstances/circonstances.service';
import { Circonstances } from '../../../../../../main/webapp/app/entities/circonstances/circonstances.model';

describe('Component Tests', () => {

    describe('Circonstances Management Detail Component', () => {
        let comp: CirconstancesDetailComponent;
        let fixture: ComponentFixture<CirconstancesDetailComponent>;
        let service: CirconstancesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SmartStatementTestModule],
                declarations: [CirconstancesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CirconstancesService,
                    JhiEventManager
                ]
            }).overrideTemplate(CirconstancesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CirconstancesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CirconstancesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Circonstances(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.circonstances).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
